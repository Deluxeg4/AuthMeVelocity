plugins {
    id("authmevelocity.publishing")
    id("authmevelocity.spotless")
}

dependencies {
    compileOnly(libs.paper)
}

tasks {
    javadoc {
        (options as StandardJavadocDocletOptions).run {
            encoding = Charsets.UTF_8.name()
            addStringOption("link", "https://jd.advntr.dev/api/${libs.versions.adventure.get()}/")
            addStringOption("link", "https://jd.advntr.dev/text-minimessage/${libs.versions.adventure.get()}/")
            addStringOption("link", "https://jd.papermc.io/paper/${libs.versions.paper.get().replace("-R0.1-SNAPSHOT", "")}/")
        }
    }  
}
