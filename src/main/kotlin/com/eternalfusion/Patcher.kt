package com.eternalfusion

import com.eternalfusion.patches.Patch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class Patcher {
    private lateinit var _fs: FileSystem
    private var patchList = mutableListOf<Patch>()

    fun getFs(): FileSystem {
        return _fs
    }

    fun loadJarFile(jar: String): File {
        val library = File(jar)

        if(!library.exists()) {
            throw FileNotFoundException(library.absolutePath);
        }

        getLocalFileSystem(library)

        return library
    }

    private fun getLocalFileSystem(file: File) {
        _fs = try {
            FileSystems.newFileSystem(file.toPath())
        } catch (e: IOException) {
            throw e
        }
    }

    fun applyPatches(vararg patches: KClass<out Patch>) {
        for (patchClass in patches) {
            val patchInstance = patchClass.primaryConstructor!!.call(this)
            patchList.add(patchInstance)
            patchInstance.applyIfCan()
            patchInstance.close()
        }
    }
}
