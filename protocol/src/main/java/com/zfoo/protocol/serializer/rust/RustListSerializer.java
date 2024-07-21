/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.protocol.serializer.rust;

import com.zfoo.protocol.generate.GenerateProtocolFile;
import com.zfoo.protocol.model.Pair;
import com.zfoo.protocol.registration.field.IFieldRegistration;
import com.zfoo.protocol.registration.field.ListField;
import com.zfoo.protocol.serializer.CodeLanguage;
import com.zfoo.protocol.serializer.CutDownListSerializer;
import com.zfoo.protocol.util.StringUtils;

import java.lang.reflect.Field;

import static com.zfoo.protocol.util.FileUtils.LS;

/**
 * @author godotg
 */
public class RustListSerializer implements IRustSerializer {

    @Override
    public Pair<String, String> fieldTypeDefaultValue(Field field, IFieldRegistration fieldRegistration) {
        var type = StringUtils.format("{}", CodeGenerateRust.toRustClassName(field.getGenericType().toString()));
        return new Pair<>(type, "Vec::new()");
    }

    @Override
    public void writeObject(StringBuilder builder, String objectStr, int deep, Field field, IFieldRegistration fieldRegistration) {
        GenerateProtocolFile.addTab(builder, deep);
        if (CutDownListSerializer.getInstance().writeObject(builder, objectStr, field, fieldRegistration, CodeLanguage.Rust)) {
            return;
        }

        ListField listField = (ListField) fieldRegistration;

        builder.append(StringUtils.format("if {}.is_empty() {", objectStr)).append(LS);
        GenerateProtocolFile.addTab(builder, deep + 1);
        builder.append("buffer.writeInt(0);").append(LS);
        GenerateProtocolFile.addTab(builder, deep);

        builder.append("} else {").append(LS);
        GenerateProtocolFile.addTab(builder, deep + 1);
        builder.append(StringUtils.format("buffer.writeInt({}.len() as i32);", objectStr)).append(LS);

        String element = "element" + GenerateProtocolFile.localVariableId++;
        GenerateProtocolFile.addTab(builder, deep + 1);
        builder.append(StringUtils.format("for {} in {}.clone() {", element, objectStr)).append(LS);
        CodeGenerateRust.rustSerializer(listField.getListElementRegistration().serializer())
                .writeObject(builder, element, deep + 2, field, listField.getListElementRegistration());
        GenerateProtocolFile.addTab(builder, deep + 1);
        builder.append("}").append(LS);
        GenerateProtocolFile.addTab(builder, deep);
        builder.append("}").append(LS);
    }

    @Override
    public String readObject(StringBuilder builder, int deep, Field field, IFieldRegistration fieldRegistration) {
        GenerateProtocolFile.addTab(builder, deep);
        var cutDown = CutDownListSerializer.getInstance().readObject(builder, field, fieldRegistration, CodeLanguage.Rust);
        if (cutDown != null) {
            return cutDown;
        }

        ListField listField = (ListField) fieldRegistration;
        String result = "result" + GenerateProtocolFile.localVariableId++;
        var typeName = CodeGenerateRust.toRustClassName(listField.getType().toString());
        builder.append(StringUtils.format("let mut {}: {} = Vec::new();", result, typeName)).append(LS);

        GenerateProtocolFile.addTab(builder, deep);
        String size = "size" + GenerateProtocolFile.localVariableId++;
        builder.append(StringUtils.format("let {} = buffer.readInt();", size)).append(LS);

        GenerateProtocolFile.addTab(builder, deep);
        builder.append(StringUtils.format("if {} > 0 {", size)).append(LS);

        GenerateProtocolFile.addTab(builder, deep + 1);
        String i = "index" + GenerateProtocolFile.localVariableId++;
        builder.append(StringUtils.format("for {} in 0 .. {} {", i, size)).append(LS);
        String readObject = CodeGenerateRust.rustSerializer(listField.getListElementRegistration().serializer())
                .readObject(builder, deep + 2, field, listField.getListElementRegistration());
        GenerateProtocolFile.addTab(builder, deep + 2);
        builder.append(StringUtils.format("{}.push({});", result, readObject)).append(LS);
        GenerateProtocolFile.addTab(builder, deep + 1);
        builder.append("}").append(LS);
        GenerateProtocolFile.addTab(builder, deep);
        builder.append("}").append(LS);
        return result;
    }
}
