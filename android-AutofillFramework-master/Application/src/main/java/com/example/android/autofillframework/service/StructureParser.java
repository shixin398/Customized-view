/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.autofillframework.service;

import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.app.assist.AssistStructure.WindowNode;
import android.util.Log;

import com.example.android.autofillframework.service.model.AutofillField;
import com.example.android.autofillframework.service.model.AutofillFieldsCollection;
import com.example.android.autofillframework.service.model.ClientFormData;
import com.example.android.autofillframework.service.model.SavableAutofillData;

import static com.example.android.autofillframework.CommonUtil.TAG;

/**
 * Parser for an AssistStructure object. This is invoked when the Autofill Service receives an
 * AssistStructure from the client Activity, representing its View hierarchy. In this sample, it
 * parses the hierarchy and collects autofill metadata from {@link ViewNode}s along the way.
 */
final class StructureParser {
    private final AutofillFieldsCollection mAutofillFields = new AutofillFieldsCollection();
    private final AssistStructure mStructure;
    private ClientFormData mClientFormData;

    StructureParser(AssistStructure structure) {
        mStructure = structure;

    }

    public void parseForFill() {
        parse(true);
    }

    public void parseForSave() {
        parse(false);
    }

    /**
     * Traverse AssistStructure and add ViewNode metadata to a flat list.
     */
    private void parse(boolean forFill) {
        Log.d(TAG, "Parsing structure for " + mStructure.getActivityComponent());
        int nodes = mStructure.getWindowNodeCount();
        mClientFormData = new ClientFormData();
        for (int i = 0; i < nodes; i++) {
            WindowNode node = mStructure.getWindowNodeAt(i);
            ViewNode view = node.getRootViewNode();
            parseLocked(forFill, view);
        }
    }

    private void parseLocked(boolean forFill, ViewNode viewNode) {
        if (viewNode.getAutofillHints() != null && viewNode.getAutofillHints().length > 0) {
            //TODO check to make sure hints are supported by service.
            if (forFill) {
                mAutofillFields.add(new AutofillField(viewNode));
            } else {
                mClientFormData.setAutofillValuesForHints
                        (viewNode.getAutofillHints(), new SavableAutofillData(viewNode));
            }
        }
        int childrenSize = viewNode.getChildCount();
        if (childrenSize > 0) {
            for (int i = 0; i < childrenSize; i++) {
                parseLocked(forFill, viewNode.getChildAt(i));
            }
        }
    }

    public AutofillFieldsCollection getAutofillFields() {
        return mAutofillFields;
    }

    public ClientFormData getClientFormData() {
        return mClientFormData;
    }
}
