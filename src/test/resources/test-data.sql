INSERT INTO poseidontest.users (username,password,fullname,enabled) VALUES
	 ('admin','$2b$10$I9J19.iKbICRLLBeI07Iuu47A80tVUw7j2eEeG/ikk2rVPX6EvmJy','Administrator',1),
	 ('user','$2b$10$Iwiv5yJ5AAnpeXwNI4Csiewr.EONg2bjTWeaR43.FHyCRDARQ7qHe','User',1);

INSERT INTO poseidontest.authorities (userid,`role`) VALUES
	 (1,'ROLE_ADMIN'),
	 (1,'ROLE_USER'),
	 (2,'ROLE_USER');

INSERT INTO poseidontest.bidlist (account,`type`,bidQuantity,askQuantity,bid,ask,benchmark,bidListDate,commentary,`security`,status,trader,book,creationName,creationDate,revisionName,revisionDate,dealName,dealType,sourceListId,side) VALUES
	 ('TESTBID_ACCOUNT','TESTBID_TYPE',12.0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);