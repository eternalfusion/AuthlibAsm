package com.eternalfusion

import com.eternalfusion.patches.PatchYggdrasilSession
import com.eternalfusion.patches.PatchYggdrasilEnvironment

fun main(args: Array<String>) {
    val patcher = Patcher()

    patcher.loadJarFile("./authlib-1.5.25.jar")
    patcher.applyPatches(PatchYggdrasilSession::class, PatchYggdrasilEnvironment::class)
    patcher.getFs().close()
}

