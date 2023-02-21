rootProject.name = "stack"

include(
    ":app",
    ":compose-markdown",
    ":compose-markdown-demo",
    ":compose-preference",
    ":dynamic-list-adapter",
    ":dynamic-list-adapter-viewbinding",
    ":markdown",
    ":stackexchange-api"
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
