package com.eternalfusion.patches

import com.eternalfusion.Patcher
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import com.eternalfusion.patches.annotations.ClassPath
import com.eternalfusion.patches.annotations.ReplaceList
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.full.findAnnotation


open class Patch(private val patcher: Patcher): AutoCloseable {
    var canApply = true
    private lateinit var patchedClass: Path
    private lateinit var classReader: ClassReader
    private var classWriter: ClassWriter? = null
    private var replaceList = mutableMapOf<String, String>()

    init {
        val classPathAnnotation = this::class.findAnnotation<ClassPath>()
            ?: throw IllegalCallerException("ClassPath annotation is required")
        val replaceListAnnotation = this::class.findAnnotation<ReplaceList>()

        replaceListAnnotation?.replaces?.forEach { it ->
            run {
                replaceList.set(it.find, it.replace)
            }
        }

        patchedClass = patcher.getFs().getPath(classPathAnnotation.path)
        if (!Files.exists(patchedClass)) {
            println("${classPathAnnotation.path} not found, skipping. Unsupported authlib version assumed")
            canApply = false
        } else {
            val input: InputStream? = try {
                Files.newInputStream(patchedClass)
            } catch (error: IOException) {
                throw error
            }

            classReader = ClassReader(input)
            classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        }
    }

    open fun apply() {}

    fun applyIfCan() {
        if (canApply) {
            apply()
        }
    }

    fun getClassReader(): ClassReader {
        return classReader
    }

    fun getClassWriter(): ClassWriter? {
        return classWriter
    }

    protected fun stringReplacer(cst: Any): String {
        return replaceList.getOrDefault(cst, cst.toString())
    }

    override fun close()  {
        if (this.classWriter == null) return
        val fOut = try {
            Files.newOutputStream(patchedClass)
        } catch (e: Exception) {
            throw e
        }
        fOut.write(classWriter!!.toByteArray())
        fOut.close()
    }

}
