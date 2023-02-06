package com.eternalfusion.patches

import com.eternalfusion.Patcher
import com.eternalfusion.patches.annotations.ClassPath

@ClassPath("com/mojang/authlib/yggdrasil/YggdrasilEnvironment.class")
class PatchYggdrasilEnvironment(patcher: Patcher) : Patch(patcher) {
    override fun apply() {
        println(getClassWriter())
    }
}
