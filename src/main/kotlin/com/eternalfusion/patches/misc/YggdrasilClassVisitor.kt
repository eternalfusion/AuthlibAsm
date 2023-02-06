package com.eternalfusion.patches.misc

import org.objectweb.asm.ClassVisitor

open class YggdrasilClassVisitor(api: Int, classVisitor: ClassVisitor?): ClassVisitor(api, classVisitor) {

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
    }
}
