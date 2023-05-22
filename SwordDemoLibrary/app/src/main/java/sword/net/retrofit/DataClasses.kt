package sword.net.retrofit

/**
 * 不变性思维  空安全思维
 */

data class RepoList(
	val count: Int = 0,
	val items: List<Repo>
)

data class Repo(
	val added_stars: String = "",
	val avatars: List<String> = listOf(),
	val desc: String = "",
	val forks: String = "",
	val lang: String = "",
	val repo: String = "",
	val repo_link: String
)

data class User(val name: String)

data class Widget(val name: String)