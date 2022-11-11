# 车控家（主控）
## 标准接口定义层（供应商服务接入）
  * ms-cpsp-smarthome-master-provider-api   
## 供应商服务接入实现层
  * ms-cpsp-smarthome-master-provider-aggr-pateo 如聚合供应商博泰接入实现
  * ms-cpsp-smarthome-master-provider-jd 如京东供应商接入实现
## 微服务应用层
  * ms-cpsp-smarthome-master-service IHU接入应用层

# 家控车（受控）
## 标准接口定义层（调用供应商接口，如果有）
  * ms-cpsp-smarthome-controlled-provider-api   
## 供应商服务接入实现层（调用供应商接口，如果有）
  * ms-cpsp-smarthome-controlled-provider-jd 如调用京东供应商接口用于验证或通知之类
## 微服务应用层
  * ms-cpsp-smarthome-controlled-service 供应商接入应用层
  


