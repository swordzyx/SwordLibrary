class SuffixFilter implements FilenameFilter {
    String suffix

    SuffixFilter(String s) {
        suffix = s
    }

    boolean accept(File dir, String name) {
        return name.endsWith(suffix)
    }

}

//创建一个新的文件夹
def moveSmali = { String destDirName, String sourDirName  ->
    def sourceDir = new File(sourDirName)

    def destinationDir = new File(destDirName)
    destinationDir.deleteOnExist()
    destinationDir.mkdir()

    File[] files = sourceDir.listFiles()
    for (f in files) {
        println f.getName()
    }
    
    def antBuild = new AntBuilder()
    for(int i=0; i<2; i++) {
        String dest = "${destinationDir}/${files[i].getName()}"
        println "move ${files[i].absolutePath} to ${dest}"
        files[i].renameTo(new File(dest))
    }
}

def getDestDirName() {
    def dexFile = new File("D:\\GitCode\\Android-Pratice\\script")
    String[] fs = dexFile.list(new SuffixFilter("dex"));
    return "classes${fs.length + 1}"
}

// println "convert dex to smali"
ProcessBuilder processBuilder = new ProcessBuilder()
// processBuilder.command("java", "-jar", "baksmali.jar", "d", "old_classes.dex", "-o", "classes")
// processBuilder.start()
// println "split smali"

// def sourDirName = "classes"
def destDirName = getDestDirName()
// moveSmali.call(destDirName, sourDirName)

//将 classes.dex 转成 smali 文件
println "convert ${destDirName} to dex"
processBuilder.command("java", "-jar", "smali.jar", "a", "${destDirName}", "-o", "${destDirName}.dex")
processBuilder.start()
