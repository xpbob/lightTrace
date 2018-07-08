# lightTrace
功能点  
1. 可以获取某个类的方法执行时间
1. 代码中如果有system.gc的调用会打印出调用的堆栈  


# 使用场景
应用整体响应时间长，但不知道方法是有多慢。一旦重启就再难复现。代码中有system.gc还不好排查，例如是第三方的包中调用。

# 使用方式
1. 进入lightTrace目录下，可以查看到有Run.bat
1. jps查看java进程id
1. Run.bat pid 要查看方法的类，例如Run.bat 30232 Bean 表示进程号为30232的进程，类名里包含bean的类输出方法的执行时间。
1. Run.bat pid 就只查看gc调用栈


# 注意
1. 现在只提供了windows的脚本，如果linux就自己java执行吧
1. 需要有JAVA_HOME的环境变量
