INSERT INTO poseidon.users (username,password,fullname,enabled) VALUES
	 ('admin','$2b$10$I9J19.iKbICRLLBeI07Iuu47A80tVUw7j2eEeG/ikk2rVPX6EvmJy','Administrator',1),
	 ('user','$2b$10$Iwiv5yJ5AAnpeXwNI4Csiewr.EONg2bjTWeaR43.FHyCRDARQ7qHe','User',1);

INSERT INTO poseidon.authorities (userid,`role`) VALUES
	 (1,'ROLE_ADMIN'),
	 (1,'ROLE_USER'),
	 (2,'ROLE_USER');