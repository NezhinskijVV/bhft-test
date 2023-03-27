package com.bhft;

import com.bhft.service.TodosIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseTest {
    public long generateId() {
        return TodosIdGenerator.INSTANCE.generate();
    }

    public void removeById(long id) {
        TodosIdGenerator.INSTANCE.remove(id);
    }

}