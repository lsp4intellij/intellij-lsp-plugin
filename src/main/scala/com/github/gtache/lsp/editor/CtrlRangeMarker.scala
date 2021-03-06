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
package com.github.gtache.lsp.editor

import java.awt.Cursor

import com.github.gtache.lsp.utils.DocumentUtils
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.RangeHighlighter
import org.eclipse.lsp4j.Location

/**
  * Class used to store a specific range corresponding to the element under the mouse when Ctrl is pressed
  *
  * @param loc    The location of the definition of the element under the mouse
  * @param editor The current editor
  * @param range  The range of the element under the mouse (represented as an hyperlink)
  */
case class CtrlRangeMarker(loc: Location, editor: Editor, range: RangeHighlighter) {

  def highlightContainsOffset(offset: Int): Boolean = {
    if (!isDefinition) range.getStartOffset <= offset && range.getEndOffset >= offset else definitionContainsOffset(offset)
  }

  def definitionContainsOffset(offset: Int): Boolean = {
    DocumentUtils.LSPPosToOffset(editor, loc.getRange.getStart) <= offset && offset <= DocumentUtils.LSPPosToOffset(editor, loc.getRange.getEnd)
  }

  /**
    * If the marker points to the definition itself
    */
  def isDefinition: Boolean = range == null

  /**
    * Removes the highlighter and restores the default cursor
    */
  def dispose(): Unit = {
    if (!isDefinition) {
      editor.getMarkupModel.removeHighlighter(range)
      editor.getContentComponent.setCursor(Cursor.getDefaultCursor)
    }
  }

}
