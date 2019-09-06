CREATE TABLE time_entries (
  id          NUMBER(19) PRIMARY KEY,
  project_id  NUMBER(19),
  user_id     NUMBER(19),
  entry_date  DATE,
  hours       NUMBER(10)
);

-- Generate ID using sequence and trigger
CREATE SEQUENCE time_entries_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER time_entries_seq_tr
 BEFORE INSERT ON time_entries FOR EACH ROW
 WHEN (NEW.id IS NULL)
BEGIN
 SELECT time_entries_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/