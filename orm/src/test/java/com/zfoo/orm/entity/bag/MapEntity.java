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

package com.zfoo.orm.entity.bag;

import com.zfoo.orm.anno.EntityCache;
import com.zfoo.orm.anno.Id;
import com.zfoo.orm.anno.Persister;
import com.zfoo.orm.model.IEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EntityCache(persister = @Persister("time30s"))
public class MapEntity implements IEntity<Long> {
    @Id
    private long id;

    private Map<String, BagItem> bagMap = new HashMap<>();

    private Map<String, Map<String, String>> baseMap = new HashMap<>();

    private Map<Long, String> longStringMap = new HashMap<>();
    private Map<Integer, String> intStringMap = new HashMap<>();
    private Map<Integer, BagItem> intBagMap = new HashMap<>();
    private Map<Integer, Map<Integer, String>> intBaseMap = new HashMap<>();

    @Override
    public Long id() {
        return id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, BagItem> getBagMap() {
        return bagMap;
    }

    public void setBagMap(Map<String, BagItem> bagMap) {
        this.bagMap = bagMap;
    }

    public Map<String, Map<String, String>> getBaseMap() {
        return baseMap;
    }

    public void setBaseMap(Map<String, Map<String, String>> baseMap) {
        this.baseMap = baseMap;
    }

    public Map<Integer, String> getIntStringMap() {
        return intStringMap;
    }

    public void setIntStringMap(Map<Integer, String> intStringMap) {
        this.intStringMap = intStringMap;
    }

    public Map<Long, String> getLongStringMap() {
        return longStringMap;
    }

    public void setLongStringMap(Map<Long, String> longStringMap) {
        this.longStringMap = longStringMap;
    }

    public Map<Integer, BagItem> getIntBagMap() {
        return intBagMap;
    }

    public void setIntBagMap(Map<Integer, BagItem> intBagMap) {
        this.intBagMap = intBagMap;
    }

    public Map<Integer, Map<Integer, String>> getIntBaseMap() {
        return intBaseMap;
    }

    public void setIntBaseMap(Map<Integer, Map<Integer, String>> intBaseMap) {
        this.intBaseMap = intBaseMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapEntity mapEntity = (MapEntity) o;
        return id == mapEntity.id && Objects.equals(bagMap, mapEntity.bagMap) && Objects.equals(baseMap, mapEntity.baseMap) && Objects.equals(longStringMap, mapEntity.longStringMap) && Objects.equals(intStringMap, mapEntity.intStringMap) && Objects.equals(intBagMap, mapEntity.intBagMap) && Objects.equals(intBaseMap, mapEntity.intBaseMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bagMap, baseMap, longStringMap, intStringMap, intBagMap, intBaseMap);
    }
}
