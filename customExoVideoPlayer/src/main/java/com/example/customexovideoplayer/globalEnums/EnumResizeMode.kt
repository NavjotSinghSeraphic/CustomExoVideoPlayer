package com.example.customexovideoplayer.globalEnums

enum class EnumResizeMode(var valueStr: String, val value: Int) {

    UNDEFINE("UNDEFINE", -1),
    FIT("Fit", 1),
    FILL("Fill", 2),
    ZOOM("Zoom", 3);

    companion object {

        operator fun get(value: String?): EnumResizeMode {

            if (value == null)
                return UNDEFINE

            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.valueStr.equals(value.trim { it <= ' ' }, ignoreCase = true)) {
                    return `val`
                }
            }
            return UNDEFINE
        }

        operator fun get(value: Int?): EnumResizeMode {

            if (value == null)
                return UNDEFINE

            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.value === value) {
                    return `val`
                }
            }
            return UNDEFINE
        }
    }
}