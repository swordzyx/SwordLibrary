package com.example.threadrxjava

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.rxjava3.functions.Function
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    val url2 = "https://api.github.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        textview.text = "正在加载"
    }

    fun requestByRxJava3() {
        val retrofit = Retrofit.Builder()
            .baseUrl(url2)
            .addConverterFactory(GsonConverterFactory.create())
            //通过 createWithScheduler 创建 RxJava3CallAdapterFactory 会将传进去的 Schedulers 保存到 RxJava3CallAdapterFactory 内部， RxJava3CallAdapterFactory 内部会调用 observable.subscribeOn(scheduler) 来自动切线程
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))//让所有的网络请求发起之前都往后台切，就不必再订阅时通过 subscribeOn 来切线程了
            .build()

        retrofit.create(Api::class.java).getRepos("wanderingsword")
            //.subscribeOn(Schedulers.io()) 注释这一句
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<MutableList<Repo?>> {
                //刚刚订阅的时候，此方法会被回调，也就是在网络请求发生之前发生，可以执行初始化操作
                //Disposable
                override fun onSubscribe(d: Disposable) {
                    textview.text = "正在请求"
                }

                override fun onSuccess(t: MutableList<Repo?>?) {
                    textview.text = "加载成功，repoName = ${t!![0]?.name}"
                }

                override fun onError(e: Throwable?) {
                    textview.text = "加载失败"
                }

            })
    }

    fun requestByRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(url2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(Api::class.java)

        api.getCallRepos("wanderingsword").enqueue(object : Callback<MutableList<Repo>> {
            override fun onResponse(
                call: Call<MutableList<Repo>>,
                response: Response<MutableList<Repo>>
            ) {
                textview.text = "加载成功，name = ${response.body()!![0].name}"
            }

            override fun onFailure(call: Call<MutableList<Repo>>, t: Throwable) {
                textview.text = "加载失败"
            }
        })
    }

    fun sourceAnalyseJust() {
        //RxJava 最简单的实现
        //立刻发送一个字符串“1”，根本会感受不到 onSubscribe 的执行。
        RxJavaPlugins.setOnSingleAssembly {
            //对 Single 执行特定的函数之后，将其返回，例如延时，重复发送等
            it.doFinally {
                println("setOnSingleAssembly doFinally: ")
            }
        }

        val single = Single.just("1")
        single.subscribe(object : SingleObserver<String?> {
            override fun onSubscribe(d: Disposable?) {
            }

            override fun onSuccess(t: String?) {
                textview.text = t
            }

            override fun onError(e: Throwable?) {

            }


        })
    }

    fun sourceAnalyseMap() {
        //将 Single<Int> 转成了 Single<String>
        val single: Single<Int> = Single.just(1)

        val singleMap = single.map(object: Function<Int, String> {
            override fun apply(t: Int): String {
                return t.toString()
            }
        })

        singleMap.subscribe(object: SingleObserver<String> {
            override fun onSubscribe(d: Disposable?) {

            }

            override fun onSuccess(t: String?) {

            }

            override fun onError(e: Throwable?) {
            }

        })
    }

    //发送无延迟有后续的事件
    fun sourceAnalyseObserableJust() {
        //无延迟，有后续的新创建事件
        Observable.just(1, 2, 3, 4).subscribe(object: Observer<Int>{
            override fun onSubscribe(d: Disposable?) {
            }

            override fun onNext(t: Int?) {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }
        })
    }

    //有延迟，有后续的新创建事件
    fun sourceAnalyseObserableInterval() {
        //有延迟有后续的新创建事件
        //每隔 1s 发送当前时间给观察者
        Observable.interval(1, 1, TimeUnit.SECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<Long?> {
            override fun onSubscribe(d: Disposable?) {

            }

            override fun onNext(t: Long?) {
                textview.text = t.toString()
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }

        })
    }
}