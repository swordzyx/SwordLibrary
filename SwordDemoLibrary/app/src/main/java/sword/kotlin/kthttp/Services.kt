package sword.kotlin.kthttp


interface GithubService {
  @GET("/repo")
  fun repos(
    @Field("lang") lang: String,
    @Field("since") since: String
  ): RepoList

  @GET("/repo")
  fun reposV2(
    @Field("lang") lang: String, 
    @Field("since") since: String
  ): KtCall<RepoList>
}