package com.eternalfusion.patches

import com.eternalfusion.Patcher
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import com.eternalfusion.patches.annotations.ClassPath
import com.eternalfusion.patches.misc.ReplaceItem
import com.eternalfusion.patches.annotations.ReplaceList
import com.eternalfusion.patches.misc.YggdrasilClassVisitor

@ClassPath("com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService.class")
@ReplaceList(
    ReplaceItem(".minecraft.net", "localhost"),
    /*ReplaceItem(".mojang.com", ".mojang.com"),*/
    ReplaceItem("https://sessionserver.mojang.com/session/minecraft/join", "https://localhost:8112/session/join"),
    ReplaceItem("https://sessionserver.mojang.com/session/minecraft/hasJoined", "https://localhost:8112/session/hasJoined")
)
class PatchYggdrasilSession(patcher: Patcher) : Patch(patcher) {

    override fun apply() {

        getClassReader().accept(object : YggdrasilClassVisitor(Opcodes.ASM7, getClassWriter()) {

            override fun visitMethod(
                access: Int,
                name: String?,
                descriptor: String?,
                signature: String?,
                exceptions: Array<out String>?
            ): MethodVisitor? {
                if (name.equals("<clinit>")) {
                    val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

                    return object : MethodVisitor(Opcodes.ASM7, mv) {
                        override fun visitLdcInsn(value: Any) {
                            val cst = stringReplacer(value)
                            println("Patching value ${value} => ${cst}")
                            super.visitLdcInsn(cst)
                        }
                    }
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions)
            }
        }, 0)
    }

}
