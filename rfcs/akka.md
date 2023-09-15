# iot_akka

- Feature Name: iot_akka
- Start Date: 2023-07-24

## Summary

这是一个使用 AKKA 实现的 IOT 规则引擎方案。

## Motivation

* 实践 AKKA
* 搭配 AKKA 实践 IOT 的规则引擎
* 学习 [Thingsboard](http://www.ithingsboard.com/docs/user-guide/rule-engine-2-0/overview/)

## Guide-level explanation

### 一、整体流程图

![image-20230726141857788](https://note-1305755407.cos.ap-nanjing.myqcloud.com/note/image-20230726141857788.png)



### 二、数据结构

~~~bash

# Actor之间的消息载体，包括了异步回调、消息类型。
trait ActorMsg {
  fn getMsgType() -> RuleEngineMsgType;
  fn getCallBack() -> CallBack;
}

# 规则引擎消息类型，分为规则引擎管理器、规则引擎、规则链节点、规则节点、规则引擎任务。
enum RuleEngineMsgType {
  RULE_ENGINE_MANAGER,
  RULE_ENGINE,
  TO_RULE_CHAIN_NODE,
  TO_RULE_NODE,
  UPDATE_RULE_NODE,
  RULE_ENGINE_TASK;
}



# 异步回调
trait Callback {
}
impl CallBack for RuleCallBack {
  fn onSuccess();
  fn onFailure(e: RuleEngineException);
}
impl CallBack for RuleNodeCallBack {
  fn onSuccess(params: List<KeyValue>);
  fn onFailure(e: RuleEngineException);
}
impl CallBack for SimpleServiceCallback {
  fn onSuccess(t: T);
  fn onFailure(e: RuleEngineException);
}



# 规则引擎节点消息
struct ToRuleMsg {
  ruleXId: String,
  hasCalled: Boolean,
  keyValues: List<KeyValue>,
  metadata: Metadata,
  Callback: Callback,
  triggerInfo:TriggerInfo,
}
struct KeyValue {
  key: String,
  value: String,
}
struct Metadata {
  orgId: Long,
  productId: Long,
  deviceId: Long,
  deviceType: Long,
  deviceName: String,
  requestId:  String,
  transactionId: String,
  eventTime: Long,
  msgType: MsgType,
  topic: String,
  params: Map<String, Object>,
}
enum MsgType {
  DEVICE_EVENT,
  DEVICE_STATUS,
  DEVICE_COMMAND,
  DEVICE_RESPONSE,
  DEVICE_ERROR,
}
struct TriggerInfo {
  id: String,
  type: TriggerType,
}
enum TriggerType {
  TASK,
  MESSAGE,
}



# Actor的上下文，包括了父Actor的ID、服务上下文、Actor的名称。
struct ActorContext {
  parentId: Long,
  serviceContext: ServiceContext,
  actorName: String,
}
# Actor的上下文的方法，包括了Actor的初始化、接收消息、Actor的启动。
trait ActorContext {
  fn preStart();
  fn onReceive(message: Object);
  fn onReceive(message: ActorMsg);
  fn initNodeContext();
}




# 服务的上下文，包括了日志服务、规则信息服务等。
struct ServiceContext {
  nodeWithDispatcher:String,
  logService: LogService,
  ruleInfoService: RuleInfoService,
}


#======================================定义三个Actor：1)AppRootActor 2)NodeActor 3)RuleChainActor=============================================================

# AppRootActor
struct AppRootActor {
  rootRuleActor: BiMap<RuleInfo, ActorRef>,
  ruleInfoService: RuleInfoService,
  logService: LogService,
}
# 规则信息，包括了规则ID、节点ID。
struct RuleInfo {
  ruleId: Long,
  nodeId: Long,
}
# AppRootActor具有ActorContext的特征。
impl ActorContext for AppRootActor {
}
# AppRootActor的方法，包括了发送消息到节点、重新加载规则。
impl AppRootActor {
  fn sendMessageToNode(triggerType: Integer, actorRef: ActorRef, nodeMsg: ToRuleMsg, nodeInfo: RuleInfo);
  fn reload(ruleId: Long, nodeId: Long);
}



#====================================== 定义两个规则引擎处理器：1)RuleNodeProcessor 2)RuleChainProcessor =============================================================

# 规则引擎处理器，包括了启动、发送消息到规则链、发送消息到规则节点。
trait IProcessor {
  fn start(context: ActorContext);
  fn toRuleChain(message: ToRuleChainMsg);
  fn toRuleNode(message: ToRuleChainMsg);
}
# 规则引擎消息
struct RuleMsg {
  state: NodeExecState,
  resultMsg: String,
  ruleCallBack: RuleCallBack,
  ruleId: Long,
  nodeId: Long,
}
impl ActorMsg for RuleMsg {
}
# 规则引擎链路消息，包括了规则引擎节点消息、消息类型、日志节点。
struct ToRuleChainMsg {
  toRuleMsg: ToRuleMsg,
  msgType: RuleEngineMsgType,
  logNode: LogNode,
}
# 规则引擎链路消息具有RuleMsg的特征。
impl RuleMsg for ToRuleChainMsg {
}



# 规则引擎节点处理器
struct RuleNodeProcessor {
  chainActor: ActorRef,
  selfActor: ActorRef,
  node: RuleNode,
  serviceContext: ServiceContext,
  nodeProcessor: NodeProcessor,
  ruleId: Long,
}
# 规则引擎节点处理器具有IProcessor的特征。
impl IProcessor for RuleNodeProcessor {
}
# 规则引擎节点处理器的方法，包括了获取处理器类。
impl RuleNodeProcessor {
  fn getClass(nodeName: String) -> Class;
}




# 规则引擎链路处理器
struct RuleChainProcessor {
    parentActor: ActorRef,
    selfActor: ActorRef,
    chain: RuleChain,
    serviceContext: ServiceContext,
    ruleId: Long,
    nodeId: Long,
    started: Boolean,
    nodeActors: Map<Long, RuleNodeCtx>,
    ruleNodeRelation: Map<Long /**规则节点ID  */, Map<NodeExecState  /***路标字符串  **/, RuleNodeRelation>>,
}
struct RuleNodeCtx {
  chainActor: ActorRef,
  selfActor: ActorRef,
  selfNode: RuleNode,
}
struct RuleNode {
  nodeId: Long,
  nodeName: String,
  context: String,
}
struct RuleNodeRelation {
  nodeId: Long,
  state: NodeExecState,
}
enum NodeExecState {
  SUCCESS,
  FAILURE,
  TIMEOUT,
}
# 规则引擎链路处理器具有IProcessor的特征。
impl IProcessor for RuleChainProcessor {
}
# 规则引擎链路处理器的方法，包括了加载Actor、获取有关联的规则引擎节点、判断是否还有下一个节点。
impl RuleChainProcessor {
  fn loadActor(context: ActorContext, node: RuleNode);
  fn getRuleNodeRelation(nodeId: Long) -> Map<NodeExecState, RuleNodeRelation>;
  fn hasNextNode(relationMap: Map<NodeExecState, RuleNodeRelation>, state: NodeExecState)
}



# NodeActor
struct NodeActor {
  processor: IProcessor,
}
# NodeActor具有ActorContext的特征。
impl ActorContext for NodeActor {
}



# RuleChainActor
struct RuleChainActor {
  processor: IProcessor,
}
# RuleChainActor具有ActorContext的特征。
impl ActorContext for RuleChainActor {
}




# 节点处理器，包括了初始化、处理消息。
trait INodeProcessor {
  fn init(ruleId: Long, node: RuleNode, serviceContext: ServiceContext);
  fn process(msg: ToRuleMsg, logNode: LogNode, serviceContext: ServiceContext, callback: RuleNodeCallBack);
}

# 节点处理器，包括了规则ID、节点ID、节点名称、服务上下文。
struct AbsNodeProcessor {
  ruleId: Long,
  nodeId: Long,
  nodeName: String,
  serviceContext: ServiceContext,
}
# 节点处理器具有INodeProcessor的特征。
impl INodeProcessor for AbsNodeProcessor {
}
# 节点处理器的方法，包括了转换Map、转换属性类型、转换节点Bean、转换操作符、转换消息类型、添加日志、判断是否为空上下文、初始化节点、处理节点消息。
impl AbsNodeProcessor {
  fn convertMap(keyValues: List<KeyValue>) -> Map<String, KeyValue>;
  fn toAttributeType(type: String) -> AttributeType;
  fn convertNodeBean(context: String, classz: Class<T>) -> T;
  fn toOptSymbol(optSymbol: String) -> OperationSymbol;
  fn toMsgType(msgType: String) -> MsgType;
  fn addLog(logNode: LogNode, msg: ToRuleMsg, state: NodeExecState, cause: String);
  fn isEmptyContext(context: String) -> Boolean;
  fn initNode(node: RuleNode);
  fn processNodeMessage(msg: ToRuleMsg, logNode: LogNode, serviceContext: ServiceContext, ruleCallBack: RuleCallBack);
}

# 属性类型
enum AttributeType {
  PRODUCT,
  DEVICE,
}
# 操作符
enum OperationSymbol {
  EQUAL,
  NOT_EQUAL,
  GREATER,
  GREATER_EQUAL,
  LESS,
  LESS_EQUAL,
  IN,
  NOT_IN,
  LIKE,
  NOT_LIKE,
  BETWEEN,
  NOT_BETWEEN,
  IS_NULL,
  IS_NOT_NULL,
}




#====================================== 定义三个节点：1)ActionNode 2)GobalFilterNode 3)TriggerAndAsNode =============================================================

# ActionNdoe节点
struct ActionNode {
  handlerChain: HandlerChain,
}

# 节点处理器链
struct HandlerChain {
  map: Map<String, List<ChainHandler>>,
}
# 节点处理器
trait ChainHandler {
  fn filter(metadata: Metadata, params: Map<String, KeyValue>, logId: Long) -> Pair<Boolean, ResultDesc>;
}
# 节点处理器处理结果，包括了请求信息、节点条件、处理器类型、原因。
struct ResultDesc {
  requestInfo: RequestInfo,
  nodeCondition: NodeCondition,
  handlerType: HandlerType,
  cause: String,
}
struct RequestInfo {
  productId: Long,
  deviceId: Long,
  msgType: MsgType,
  params: Map<String, KeyValue>,
}
struct NodeCondition {
  jsonNode: ObjectNode,
}
enum HandlerType {
  FILTER_TIMERANGE,
  FILTER_TIMEFREQUENCY,
  FILTER_DATETIMERANGE,
}

# 节点处理器链方法，包括了过滤、添加到处理器链最后。
impl HandlerChain {
  fn filter(groupName: String, params: Map<String, KeyValue>, metadata: Metadata) -> Pair<Boolean, ResultDesc>;
  fn filter(groupName: String, params: Map<String, KeyValue>, metadata: Metadata, symbol: RelationSymbol, logNode: LogNode) -> Pair<Boolean, ResultDesc>;
  fn addLast(groupName: String, handler: ChainHandler);
}


# ActionNode节点具有AbsNodeProcessor的特征。
impl AbsNodeProcessor for ActionNode {
}
# ActionNode节点的方法，包括了转换处理器链、处理消息。
impl ActionNode {
  fn convertToHandlerChain(rootBean: ActionRootBean);
  fn process(msg: ToRuleMsg, serviceContext: ServiceContext, logNode: LogNode);
}
struct ActionRootBean {
  _comment: String,
  deviceMsg: List<DeviceMsg>,
  alarm: Alarm,
}
struct Alarm {
  log: SysLog,
}
struct SysLog {
  level: String,
  title: String,
  code: String,
  linkRuleId: Long,
}



# GobalFilterNode节点
struct GobalFilterNode {
  handlerChain: HandlerChain,
  messageFlags: List<String>,
}
# GobalFilterNode节点具有AbsNodeProcessor的特征。
impl AbsNodeProcessor for GobalFilterNode {
}
# GobalFilterNode节点的方法，包括了转换处理器链、获取消息列表、处理消息。
impl GobalFilterNode {
  fn convertToHandlerChain(rootBean: ActionRootBean);
  fn getMessageList(devices: List<Device>) -> List<DeviceMessage>;
  fn process(msg: ToRuleMsg, serviceContext: ServiceContext, logNode: LogNode);
}

struct Device {
  _comment: String,
  msgType: String,
  typeVal: String,
  optKey: String,
  optSymbol: String,
  optVal: String,
}
struct DeviceMessage {
  msgType: MsgType,
  type: AttributeType,
  typeVal: String,
  optKey: String,
  optSymbol: OperationSymbol,
  optVal: String
}

~~~


~~~bash
#===================================== 数据库结构 ==============================

# 对应首Actor：RuleChainActor
RuleDO {
   ruleId: Long,
   nodeId: Long,
   status: Integer,
}

# 对应子Actor：NodeActor
RuleNodeDO {
   nodeId: Long,
   nodeName: String,
   context: String, ## 节点内容
}

# 首Actor和子Actor的关联
RuleRefNodeDO {
   chainId: Long, ## 主键
   nodeId: Long, ## 头节点
   refNodeId: Long,	## 尾节点
   exeTag: String, ## 流向条件，如succeed,failed
}

~~~



## Reference-level explanation

### 一、Actor 详细设计
设计三个 Actor，分别是

* AppRootActor
* RuleChainActor
* NodeActor。



它们之间的关系，如下图所示：

![image-20230726113919639](https://note-1305755407.cos.ap-nanjing.myqcloud.com/note/image-20230726113919639.png)



它们的消息处理流程，如下图所示：

注意，消息类型经过【RuleChainActor】时从【RuleChainMsg】变成【RuleNodeMsg】，意思是让【RuleChainActor】将消息发给其它【NodeActor】

![image-20230811170542863](https://note-1305755407.cos.ap-nanjing.myqcloud.com/note/image-20230811170542863.png)



这是一个【有状态】的设计，ChildActor 处理完成后，通知 ParentActor。

#### 1、AppRootActor

> 它的消息来源是上游服务。

它的作用是将收到的消息发送到 RuleChainActor。



#### 2、RuleChainActor

> 它的消息来源是父节点 AppRootActor 和子节点 NodeActor。

它可以处理多种消息，根据不同的消息选择不通的处理。

流程如下：

* 如果是规则引擎链路消息，通过处理器对链路消息进行处理。
* 如果是规则引擎节点消息，通过处理器对节点消息进行处理。



#### 3、NodeActor

> 它的消息来源是父节点 RuleChainActor。

它不关心消息类型，只关心具体的业务处理。每一个 NodeActor 对应一个规则引擎节点，用来处理对应的业务。



### 二、规则引擎处理器

设计两个规则引擎处理器，分别是：

* RuleChainProcessor
* RuleNodeProcessor



处理器处理的消息类型，分别是：

* TO_RULE_CHAIN_NODE，规则引擎链路消息
* TO_RULE_NODE，规则引擎节点消息





#### 1、规则引擎链路处理器-RuleChainProcessor
> 对应处理 RuleChainActor 的收到的消息。



【规则引擎链路消息】的处理流程：
1. 收到消息。
2. 直接将消息发送到对应的 NodeActor。




【规则引擎节点消息】的处理流程：
1. 收到消息。
2. 查找有关联的规则引擎节点。
3. 如果有关联时，就将消息发送到关联的 NodeActor。
4. 如果没有关联时，响应父节点。



【注意】RuleChainActor 的父节点是 AppRootActor。




#### 2、规则引擎节点处理器-RuleNodeProcessor
> 对应处理 NodeActor 的收到的消息。



处理流程：

1. 收到消息。
2. 找到对应的节点，进行业务处理。
3. 响应父节点。
   * 节点处理消息成功时，1）修改消息类型为规则引擎节点消息，状态为成功 2）发送消息给父节点。
   * 节点处理消息失败时，1）修改消息类型为规则引擎节点消息，状态为失败 2）发生消息给父节点。





【注意】NodeActor 的父节点是 RuleChainActor 。




【问题】为什么要处理完成后，不管成功还是失败，都要发送消息给父节点呢？
* 因为这里设计的是一个有状态的系统。
* 所以需要发生消息给父节点，让父节点知道当前节点的状态，从而决定下一步的操作。如成功则转发下一节点、失败则重传等。
* 参考【ApacheFlink有状态、ApacheStorm无状态】




### 三、规则节点
设计三个规则节点，对应处理 NodeActor 的业务。
* ActionNode
* GobalFilterNode
* TriggerAndAsNode



【注意】规则节点中的业务处理方法，处理完成需要回调，这个回调很重要，要用来标记可以将消息发送到下一个节点了。

意思就是，当前节点处理完后，【什么时候需要给到下一个节点呢？】【什么时候需要重传？】就是当当前节点告诉链路处理完成了。






### 四、详细设计
#### 1、Actor 的抽象设计
因为三个 Actor 都有初始化、接收消息的操作，现在我希望执行这些操作之前，能够做一些相同的前置/后置操作，于是抽象出 ActorContext 来定义初始化、接收消息。

流程如下：

1. 定义 ActorContext，继承 UntypedAbstractActor。在 preStart 中调用 initNodeContext，onReceive 中调用 onReceive。并且增加前置/后置操作。
2. 将 Actor 从直接继承 UntypedAbstractActor，改成继承 ActorContext，并且实现【initNodeContext】、【onReceive】。



#### 2、Actor 的初始化

【preStart】 方法是 UntypedAbstractActor 的生命周期方法之一，它会在 Actor 第一次启动时被调用。

意思就是，【Props.create】或者【ActorSystem.create】的时候触发。



所以，我们的初始化流程是这样的：

* 项目启动的时候，先创建 AppRootActor，并初始化。
* AppRootActor 初始化的时候，创建所有关联的 RuleChainActor，并初始化。
* RuleChainActor 初始化的时候，创建所有关联的 NodeActor，并初始化。



![image-20230811163217058](https://note-1305755407.cos.ap-nanjing.myqcloud.com/note/image-20230811163217058.png)



#### 3、关联节点设计

当规则节点生成输出消息时，它总是将消息路由到下一个指定的节点并通过【关系类型】进行关联。

* 表示成功与否的规则节点关系是 **Success** 和 **Failure** 。
* 表示逻辑运算的规则节点可以是 **True** 或 **False** 。
* 一些特定的规则节点可能使用完全不同的关系类型例如：“Post Telemetry”、“Attributes Updated”、“Entity Created”等。



如下所示：

![image-20230814115257974](https://note-1305755407.cos.ap-nanjing.myqcloud.com/note/image-20230814115257974.png)



#### 4、规则节点设计--采用工厂模式

输入是节点名称，比如 action、filter、triger 等，我们要得到的输出是一个具体的节点实例。

* 采用简单工厂模式，可以通过传入不同的参数类型来构建不同的对象实例。
* 工厂方法的逻辑实现采用 java 的反射。






## Drawbacks

N/A

## Rationale and alternatives

N/A

## Prior art

N/A

## Unresolved questions

N/A

## Future possibilities

#### 1、目前节点之间的关系类型只有 Success 和 Failure，需要后续完善。

