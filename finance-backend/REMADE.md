
1.配置mybatis
1.1.测试mybatis-generator

2.配置SpringSecurity
2.1.用户认证
2.1.1 用户名密码 
获取用户凭证UsernamePasswordAuthenticationFilter
通过获取用户凭证DaoAuthenticationProvider进行相应的认证

2.1.2 JwtToken
获取JwtToken JwtAuthenticationFilter
通过jwtTOken  JwtAuthenticationProvider 进行响应的认证

2.2.实现UserDetailService
通过username获取用户信息提供给AuthenticationProvider使用

##### 2.2.权限过滤

3.开始业务