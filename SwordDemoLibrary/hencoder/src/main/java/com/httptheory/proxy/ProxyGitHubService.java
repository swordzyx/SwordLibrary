package com.httptheory.proxy;

import com.sword.httptheory.retrofit.Repo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import retrofit2.Call;
//import retrofit2.Platform;

//Retrofit.create(GitHubService::class.java) 动态创建出来的代理就跟 ProxyGitHubService 是相似的。
//可以认为 Proxy.newProxyInstance 会创建 ProxyGithubService 对象，它是 GitHubService 的代理，然后再 ProxyGithubService 中，实际执行操作的是 InvocationHandler 对象。
public class ProxyGitHubService/* implements GitHubService */{
  
  /*//Proxy.newInstanceProxy 中有一个 InvocationHandler，就跟下面的是一样的，然后在实现 GitHubService 中的方法时，会去调用 handler.invoke()。InvocationHandler 是实际做操作的对象。
  InvocationHandler invocationHandler = new InvocationHandler() {
    private final Platform platform = Platform.get();
    private final Object[] emptyArgs = new Object[0];

    @Override
    public @Nullable Object invoke(Object proxy, Method method, @Nullable Object[] args)
                  throws Throwable {
      // If the method is a method from Object then defer to normal invocation.
      if (method.getDeclaringClass() == Object.class) {
        return method.invoke(this, args);
      }
      args = args != null ? args : emptyArgs;
      return platform.isDefaultMethod(method)
          ? platform.invokeDefaultMethod(method, service, proxy, args)
          : loadServiceMethod(method).invoke(args);
    }
  };

  *//*每次执行 GitHubService 中的方法，实际执行会执行 InvocationHandler#invoke() 方法*//*
  @Override
  public Call<List<Repo>> listRepos(String user) {

    Method method = null;
    try {
      //method = GitHubService.class.getDeclaredMethod("listRepos", String.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    //invoke 第一个参数是代理对象，也就是 ProxyGithubService 本身；第二个参数就是代理的方法，是一个 Method 对象；第三个参数是方法的参数，也就是 listRepos(..) 方法的参数
    return invocationHandler.invoke(this, method, user);
  }*/

}
