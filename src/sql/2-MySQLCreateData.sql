--------------------------------------------------------------------------------
-- INSERT statements for inserting data required by the application
-- in the "pojo" database.
--------------------------------------------------------------------------------

INSERT INTO CategoryInfo (categoryName)
    VALUES ('Baloncesto'),
            ('Tenis'),
            ('Fútbol'),
            ('Boxeo'),
            ('Motor');

/* INSERT INTO EventInfo (eventId, eventName, eventDate)
    VALUES (1, 'evento1', '2016-04-30 18:00:00'),
            (2, 'evento2', '2016-04-30 18:00:00'),
            (3, 'evento3', '2016-04-30 18:00:00');

INSERT INTO BetInfo (betId, betDate, amount, userId, optionId)
    VALUES (1, '2016-04-30 18:00:00', 100, 1, 2);

INSERT INTO TypeOption (typeId, odd, result)
    VALUES (1, 100, 'Barça');
*/