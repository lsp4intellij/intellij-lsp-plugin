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
package com.github.gtache.lsp.editor.listeners

import com.github.gtache.lsp.PluginMain
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.event.{EditorFactoryEvent, EditorFactoryListener}

/**
  * An EditorListener implementation
  */
class EditorListener extends EditorFactoryListener {

  private val LOG: Logger = Logger.getInstance(classOf[EditorListener])

  override def editorReleased(editorFactoryEvent: EditorFactoryEvent): Unit = {
    PluginMain.editorClosed(editorFactoryEvent.getEditor)
  }

  override def editorCreated(editorFactoryEvent: EditorFactoryEvent): Unit = {
    PluginMain.editorOpened(editorFactoryEvent.getEditor)
  }
}