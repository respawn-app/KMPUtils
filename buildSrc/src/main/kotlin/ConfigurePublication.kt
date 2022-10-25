import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.provideDelegate

fun Project.configurePublication() {

    afterEvaluate {

        extensions.findByType<PublishingExtension>()?.apply {
            repositories {
                maven {
                    url = uri(
                        if (isReleaseBuild) {
                            "https://oss.sonatype.org/service/local/staging/deploy/maven2"
                        } else {
                            "https://oss.sonatype.org/content/repositories/snapshots"
                        }
                    )
                    credentials {
                        username = requireNotNull(properties["sonatypeUsername"]).toString()
                        password = requireNotNull(properties["sonatypePassword"]).toString()
                    }
                }
            }

            publications.withType<MavenPublication>().configureEach {
                // TODO: Javadoc

                pom {
                    name.set("KMMUtils")
                    description.set("A collection of Kotlin Multiplatform essentials")
                    url.set("https://github.com/nek-12/kmmutils")

                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("nek-12")
                            name.set("Nikita Vaizin")
                        }
                    }
                    scm {
                        url.set("https://github.com/nek-12/kmmutils")
                    }
                }
            }
        }

        extensions.findByType<SigningExtension>()?.apply {
            val publishing = extensions.findByType<PublishingExtension>() ?: return@apply
            val key = properties["signingKey"]?.toString()?.replace("\\n", "\n")
            val password = properties["signingPassword"]?.toString()

            useInMemoryPgpKeys(key, password)
            sign(publishing.publications)
        }

        tasks.withType<Sign>().configureEach {
            onlyIf { isReleaseBuild }
        }
    }
}
