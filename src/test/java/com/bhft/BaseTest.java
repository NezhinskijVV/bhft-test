package com.bhft;

import com.bhft.service.TodosIdGenerator;

public class BaseTest {
    public long generateId() {
        return TodosIdGenerator.INSTANCE.generate();
    }

    public void removeById(long id) {
        TodosIdGenerator.INSTANCE.remove(id);
    }

}