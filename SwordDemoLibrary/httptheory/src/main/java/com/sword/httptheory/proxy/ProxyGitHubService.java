package com.sword.httptheory.proxy;

import com.sword.httptheory.retrofit.GitHubService;
import com.sword.httptheory.retrofit.Repo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import retrofit2.Call;
import retrofit2.Platform;

//Retrofit.create(GitHubService::class.java) 动态创建出来的代理就跟 ProxyGitHubService 是相似的。
public class ProxyGitHubService implements GitHubService {

  //Proxy.newInstanceProxy 中有一个 InvocationHandler，就跟下面的是一样的，然后在实现 GitHubService 中的方法时，会去调用 handler.invoke()。InvocationHandler 是实际做操作的人。
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

  @Override
  public Call<List<Repo>> listRepos(String user) {
    return invocationHandler.invoke();
  }

}
