/**
 *     Copyright 2017-2018 Guillaume Tâche
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.github.gtache.lsp.actions

import com.github.gtache.lsp.PluginMain
import com.github.gtache.lsp.editor.EditorEventManager
import com.intellij.codeInsight.actions.{LayoutCodeDialog, ShowReformatFileDialog, TextRangeType}
import com.intellij.lang.LanguageFormatting
import com.intellij.openapi.actionSystem.{AnActionEvent, CommonDataKeys}
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiDocumentManager

/**
  * Class overriding the default action handling the Reformat dialog event (CTRL+ALT+SHIFT+L by default)
  * Fallback to the default action if the language is already supported or not supported by any language server
  */
class LSPShowReformatDialogAction extends ShowReformatFileDialog with DumbAware {
  private val HELP_ID = "editing.codeReformatting"

  private val LOG: Logger = Logger.getInstance(classOf[LSPShowReformatDialogAction])

  override def actionPerformed(e: AnActionEvent): Unit = {
    val editor = e.getData(CommonDataKeys.EDITOR)
    val project = e.getData(CommonDataKeys.PROJECT)
    if (editor != null) {
      val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument)
      if (LanguageFormatting.INSTANCE.allForLanguage(file.getLanguage).isEmpty && PluginMain.isExtensionSupported(FileDocumentManager.getInstance().getFile(editor.getDocument).getExtension)) {

        val hasSelection = editor.getSelectionModel.hasSelection
        val dialog = new LayoutCodeDialog(project, file, hasSelection, HELP_ID)
        dialog.show()

        if (dialog.isOK) {
          val options = dialog.getRunOptions
          EditorEventManager.forEditor(editor).foreach(manager => if (options.getTextRangeType == TextRangeType.SELECTED_TEXT) manager.reformatSelection() else manager.reformat())
        }
      } else {
        super.actionPerformed(e)
      }
    } else {
      super.actionPerformed(e)
    }
  }

  override def update(event: AnActionEvent): Unit = super.update(event)
}
