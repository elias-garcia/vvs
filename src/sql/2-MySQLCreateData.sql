-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "pojo" database.
-------------------------------------------------------------------------------

INSERT into UserProfile
    VALUES (1,'admin','UDn64bBABog2A','admin','admin','admin@udc.es'),
            (2,'user','MXYdDUCN1h84I','user','demo','user@udc.es');

INSERT INTO CategoryInfo (categoryId, categoryName)
    VALUES (1, 'Baloncesto'),
            (2, 'Fútbol'),
            (3, 'Tenis'),
            (4, 'Boxeo'),
            (5, 'Motor');

INSERT INTO EventInfo
    VALUES  (1, 'R.Madrid - FC Barcelona', '2017-4-2', 2);  
INSERT INTO EventInfo
    VALUES  (2, 'España - República Checa', '2017-6-13', 2);
INSERT INTO EventInfo
    VALUES  (3, 'R.Madrid - Getafe','2017-6-13', 2);
INSERT INTO EventInfo
    VALUES  (4, 'Villareal - Deportivo', '2017-8-7', 2);
INSERT INTO EventInfo
    VALUES  (5, 'Getafe - Sporting', '2017-8-7', 2);
INSERT INTO EventInfo
    VALUES  (6, 'Eibar - Betis', '2017-8-7', 2);
INSERT INTO EventInfo
    VALUES  (7, 'Barcelona - Espanyol', '2017-8-8', 2);
INSERT INTO EventInfo
    VALUES  (8, 'Sevilla - Granada', '2017-8-8', 2);
INSERT INTO EventInfo
    VALUES  (9, 'Las palmas - Athelic', '2017-8-8', 2);
INSERT INTO EventInfo
    VALUES  (10, 'Celta - Malaga', '2017-8-8', 2);    
INSERT INTO EventInfo
    VALUES  (11, 'Levante - Atlético', '2017-8-8', 2);
INSERT INTO EventInfo
    VALUES  (12, 'Toronto Raptors - Miami Heat', '2017-7-5', 1);  
INSERT INTO EventInfo
    VALUES  (13, 'LA Lakers - Cavs', '2017-7-5', 1);
INSERT INTO EventInfo
    VALUES  (14, 'R.Madrid - Valencia', '2017-6-13', 2);

------------------------------------------------------
-------- BetTpes en los EventInfoos 1,2 y 14 -------------
------------------------------------------------------

INSERT INTO BetType
    VALUES  (1, '¿Quién ganará el encuentro?', false, false, 1);
INSERT INTO BetType
    VALUES  (2, '¿Quién ganará el encuentro?', false, false, 2);
INSERT INTO BetType
    VALUES  (3, '¿Cuantos goles se marcarán en el 1er tiempo?', false, false, 2);
INSERT INTO BetType
    VALUES  (4, '¿Cuantos goles se marcarán en el 2º tiempo?', false, false, 2);
INSERT INTO BetType
    VALUES  (5, '¿Será el número de goles par o impar?', false, false, 2);
INSERT INTO BetType
    VALUES  (6, 'Resultado exacto', false, false, 2);
INSERT INTO BetType
    VALUES  (7, 'Resultado al descanso', false, false, 2);
INSERT INTO BetType
    VALUES  (8, '¿Qué equipo ganará ambos tiempos?', false, false, 2);
INSERT INTO BetType
    VALUES  (9, '¿Cuántos goles marcará el primer equipo?', false, false, 2);
INSERT INTO BetType
    VALUES  (10, '¿Cuántos goles marcará el segundo equipo?', false, false, 2);
INSERT INTO BetType
    VALUES  (11, '¿Marcará el primer equipo en ambos tiempos?', false, false, 2);
INSERT INTO BetType
    VALUES  (12, '¿Marcará el segundo equipo en ambos tiempos?', false, false, 2);
INSERT INTO BetType
    VALUES  (13, '¿Quién ganará el encuentro?', false, false, 2);
INSERT INTO BetType
    VALUES  (14, '¿Quién ganará el encuentro?', false, false, 14);
INSERT INTO BetType
    VALUES  (15, '¿Cuantos goles se marcarán?', false, false, 14);
INSERT INTO BetType
    VALUES  (16, '¿Quén marcará el primer gol?', false, false, 14);
INSERT INTO BetType
    VALUES  (17, '¿El resultado del partido será par o impar?', false, false, 14);
INSERT INTO BetType
    VALUES  (18, '¿Quién ganará el encuentro? MÚLTIPLE', true, false, 1);

----------------------------------------------------------
-------- TypeOptions en los BetTypes 1,2 y 14 -------------
----------------------------------------------------------

INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('1', 1.90 , NULL, 1);
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('X', 2.20 , NULL, 1);   
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('2', 2.70 , NULL, 1); 


INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('1', 1.90 , NULL, 2);
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('X', 2.20 , NULL, 2);   
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('2', 2.70 , NULL, 2); 
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Más de 0,5', 3.20 , NULL, 3);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Menos de 0,5', 4.00 , NULL, 3); 
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Más de 1,5', 3.20 , NULL, 3);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Menos de 1,5', 4.00 , NULL, 3); 
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Más de 2,5', 3.20 , NULL, 3);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Menos de 2,5', 4.00 , NULL, 3);
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Más de 3,5', 3.20 , NULL, 3);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Menos de 3,5', 4.00 , NULL, 3);
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Más de 4,5', 3.20 , NULL, 3);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Menos de 4,5', 4.00 , NULL, 3); 
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Más de 5,5', 3.20 , NULL, 3);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Menos de 5,5', 4.00 , NULL, 3);
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Más de 6,5', 3.20 , NULL, 3);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Menos de 6,5', 4.00 , NULL, 3);

INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('1', 1.90 , NULL, 14);
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('X', 2.20 , NULL, 14);   
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('2', 2.70 , NULL, 14); 

INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Más de 0,5', 3.20 , NULL, 15);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Menos de 0,5', 4.00 , NULL, 15); 
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Más de 1,5', 3.20 , NULL, 15);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Menos de 1,5', 4.00 , NULL, 15); 
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Más de 2,5', 3.20 , NULL, 15);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Menos de 2,5', 4.00 , NULL, 15);

INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Cristiano Ronaldo', 1.90 , NULL, 16);
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Karim Benzema', 2.20 , NULL, 16);   
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Gareth Bale', 2.70 , NULL, 16);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Alvaro Negredo', 3.20 , NULL, 16);  
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Paco Alcácer', 4.00 , NULL, 16);  

INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Par', 1.95 , NULL, 17);
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('Impar', 2.25 , NULL, 17);   

INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('1', 1.90 , NULL, 18);
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('X', 2.20 , NULL, 18);   
INSERT INTO TypeOption (result,odd,isWinner,typeId)
    VALUES  ('2', 2.70 , NULL, 18);

-----------------------------------------------------------
-------- Bets para los usuarios en las grids --------------
-----------------------------------------------------------

INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);
INSERT INTO BetInfo (amount,betDate,userId,optionId) values (1,CURRENT_TIMESTAMP,2,1);



