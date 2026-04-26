plugins {
    id("authmevelocity.publishing")
    id("authmevelocity.spotless")
}

dependencies {
    compileOnly(libs.velocity.api)
}

tasks {
    javadoc {
        (options as StandardJavadocDocletOptions).run {
            encoding = Charsets.UTF_8.name()
            addStringOption("link", "https://jd.advntr.dev/api/${libs.versions.adventure.get()}/")
            addStringOption("link", "https://jd.advntr.dev/text-minimessage/${libs.versions.adventure.get()}/")
            addStringOption("link", "https://jd.papermc.io/velocity/${libs.versions.velocity.get().replace("-SNAPSHOT", "")}/")
        }
    }
}
