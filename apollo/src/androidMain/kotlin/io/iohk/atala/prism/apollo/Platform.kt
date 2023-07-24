package io.iohk.atala.prism.apollo

public actual object Platform {
    public actual val OS: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}
