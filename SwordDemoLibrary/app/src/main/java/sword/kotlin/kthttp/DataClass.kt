package sword.kotlin.kthttp

data class RepoList(var count: Int?, var items: List<Repo>?, var msg: String?)

data class Repo(
  var added_stars: String?,
  var avatars: List<String>?,
  var desc: String?,
  var forks: String?,
  var lang: String?,
  var repo: String?,
  var repo_link: String?,
  var stars: String?
)