/*
 * Copyright (c) Interactive Information R & D (I2RD) LLC.
 * All Rights Reserved.
 *
 * This software is confidential and proprietary information of
 * I2RD LLC ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered
 * into with I2RD.
 */

package net.venturetech.event_api.note.ui;


import net.venturetech.event_api.note.model.Note;

import net.proteusframework.core.locale.TextSource;
import net.proteusframework.core.locale.TextSources;
import net.proteusframework.ui.miwt.component.Field;
import net.proteusframework.ui.miwt.component.composite.editor.TemplateCompositeValueEditor;
import net.proteusframework.ui.miwt.component.composite.editor.TextEditor;
import net.proteusframework.ui.miwt.component.template.FileSystemTemplateDataSource;

/**
 * ValueEditor for Note
 *
 * @author Alan Holt (aholt@venturetech.net)
 * @since 2/4/16 9:36 AM
 */
public class NoteValueEditor extends TemplateCompositeValueEditor<Note>
{
    private boolean _treatEmptyAsValid;
    private TextSource _label;

    /**
     * Instantiates a new instance of NoteValueEditor
     */
    public NoteValueEditor()
    {
        super(Note.class, new FileSystemTemplateDataSource("note/NoteValueEditor.xml"));

        addClassName("note-val-editor");
    }

    @Override
    public void init()
    {
        super.init();

        addEditorForProperty(() -> {
            TextEditor editor = new TextEditor(_label == null ? TextSources.EMPTY : _label, null);
            final Field valueComponent = editor.getValueComponent();
            valueComponent.setMaxChars(4000);
            valueComponent.setDisplayWidth(75);
            valueComponent.setDisplayHeight(10);
            if (!_treatEmptyAsValid)
            {
                editor.setRequiredValueValidator();
            }
            editor.addClassName("note-content");
            editor.setComponentName("note-content");
            return editor;
        }, Note::getContent, Note::setContent);

        applyTemplate();
    }

    /**
     * Set boolean flag -- if true, will allow for an empty string to be saved
     *
     * @param treatEmptyAsValid boolean flag
     */
    public void setTreatEmptyAsValid(boolean treatEmptyAsValid)
    {
        _treatEmptyAsValid = treatEmptyAsValid;
    }

    /**
     * Set the label on this editor
     *
     * @param label the Label
     *
     * @return this
     */
    public NoteValueEditor withLabel(TextSource label)
    {
        setLabel(label);
        return this;
    }

    /**
     * Set the label on this editor
     *
     * @param label the Label
     */
    public void setLabel(TextSource label)
    {
        _label = label;
    }
}
