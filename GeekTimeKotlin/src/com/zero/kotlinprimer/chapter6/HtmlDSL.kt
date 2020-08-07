import java.io.File

fun main(args: Array<String>) {
    val result =
        //这是一个 Lambda 表达式
        html {
            //调用 head() 方法，head() 方法接收一个 Lambda 表达式
            head {
                title { +"HTML encoding with Kotlin" }
            }
            //调用 body() 方法，body 接收一个 Lambda 表达式，没有参数，因此小括号可以省略
            body {
                h1 { +"HTML encoding with Kotlin" }
                p { +"this format can be used as an alternative markup to HTML" }

                // an element with attributes and text content
                a(href = "http://jetbrains.com/kotlin") { +"Kotlin" }

                // mixed content
                p {
                    "This is some"{
                        "color" `=` "green"
                    }
                    b { +"mixed" }
                    +"text. For more see the"
                    a(href = "http://jetbrains.com/kotlin") { +"Kotlin" }
                    +"project"
                }
                p { +"some text" }

                // content generated from command-line arguments
                p {
                    +"Command line arguments were:"
                    ul {
                        for (arg in args)
                            li { +arg }
                    }
                }
            }
        }

    val htmlFile = File("test.html")
    htmlFile.writeText(result.toString())
}

//HTML 最顶层的接口
interface Element {
    fun render(builder: StringBuilder, indent: String)
}

//专用于一个独立的字符串
class TextElement(val text: String) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }
}

//对应 html 中的标签
abstract class Tag(val name: String) : Element {
    val children = arrayListOf<Element>()
    val attributes = hashMapOf<String, String>()

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent<$name${renderAttributes()}>\n")
        for (c in children) {
            c.render(builder, indent + "  ")
        }
        builder.append("$indent</$name>\n")
    }

    private fun renderAttributes(): String? {
        val builder = StringBuilder()
        for (a in attributes.keys) {
            builder.append(" $a=\"${attributes[a]}\"")
        }
        return builder.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }
}

//包括 Text 文本，TagWithText 会有若干个子类
abstract class TagWithText(name: String) : Tag(name) {
    val textAttribute = HashMap<String, String>()
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }

    //这表示 String 在调用构造函数时会调用这一段代码
    operator fun String.invoke(block: () -> Unit) {
        block()
        if (!textAttribute.isEmpty()){
            children.add(FONT(this, textAttribute))
        }else{
            children.add(TextElement(this))
        }
    }

    infix fun String.`=`(s: String) {
        textAttribute.put(this, s)
    }
}

class HTML() : TagWithText("html") {
    //前面加上了 Head. 表示只有 Head 这个类才能调用这个方法，也就是只能传 Head 类中的方法，作为参数
    fun head(init: Head.() -> Unit) = initTag(Head(), init)

    fun body(init: Body.() -> Unit) = initTag(Body(), init)
}

class Head() : TagWithText("head") {
    fun title(init: Title.() -> Unit) = initTag(Title(), init)
}

class Title() : TagWithText("title")

class FONT(text: String, attribute: HashMap<String, String>): BodyTag("font"){
    init {
        attributes.putAll(attribute)
        children.add(TextElement(text))
    }
}

abstract class BodyTag(name: String) : TagWithText(name) {
    fun b(init: B.() -> Unit) = initTag(B(), init)
    fun p(init: P.() -> Unit) = initTag(P(), init)
    fun h1(init: H1.() -> Unit) = initTag(H1(), init)
    fun ul(init: UL.() -> Unit) = initTag(UL(), init)
    fun a(href: String, init: A.() -> Unit) {
        val a = initTag(A(), init)
        a.href = href
    }

}

class Body() : BodyTag("body")
class UL() : BodyTag("ul") {
    fun li(init: LI.() -> Unit) = initTag(LI(), init)
}

class B() : BodyTag("b")
class LI() : BodyTag("li")
class P() : BodyTag("p")
class H1() : BodyTag("h1")

class A() : BodyTag("a") {
    public var href: String
        get() = attributes["href"]!!
        set(value) {
            attributes["href"] = value
        }
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()
    html.init()
    return html
}