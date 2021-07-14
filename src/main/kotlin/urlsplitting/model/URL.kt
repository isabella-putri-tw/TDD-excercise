package urlsplitting.model

data class URL(val protocol: String, val domain: String, val path: String? = null)
