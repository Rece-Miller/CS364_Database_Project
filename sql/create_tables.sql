/* ----------------------------------------------------- */
/* Main tables */
/* ----------------------------------------------------- */

CREATE TABLE Galaxy (
	GalaxyId INT PRIMARY KEY,
	Name VARCHAR(100),
	Type VARCHAR(50),
	Radius DOUBLE,
	DistanceToEarth DOUBLE
);

CREATE TABLE Star (
	StarId INT PRIMARY KEY,
	Name VARCHAR(100),
	Radius DOUBLE,
	Color VARCHAR(50),
	Mass DOUBLE,
	SurfaceTemperature DOUBLE,
	Luminosity DOUBLE
);

CREATE TABLE Planet (
	PlanetId INT PRIMARY KEY,
	Name VARCHAR(100),
	Radius DOUBLE,
	Mass DOUBLE,
	AtmosphericDensity DOUBLE,
	Classification VARCHAR(50)
);

CREATE TABLE Element (
	NumProtons INT PRIMARY KEY,
	Name VARCHAR(50),
	Symbol VARCHAR(3),
	AverageAtomicMass DOUBLE,
	Type VARCHAR(50)
);


/* ----------------------------------------------------- */
/* One to many */
/* ----------------------------------------------------- */

-- 1 galaxy has many black holes
CREATE TABLE BlackHole (
	BlackHoleId INT PRIMARY KEY,
	Name VARCHAR(100),
	Type VARCHAR(50),
	Mass DOUBLE,
	Radius DOUBLE,
	GalaxyId INT,
	FOREIGN KEY (GalaxyId) REFERENCES Galaxy(GalaxyId)
);

-- 1 planet has many moons
CREATE TABLE Moon (
	MoonId INT PRIMARY KEY,
	Name VARCHAR(100),
	Radius DOUBLE,
	Mass DOUBLE,
	AtmosphericDensity DOUBLE,
	PlanetId INT,
	OrbitTime DOUBLE,
	IsTidallyLocked BOOLEAN,
	AvgOrbitDistance DOUBLE,
	FOREIGN KEY (PlanetId) REFERENCES Planet(PlanetId)
);


/* ----------------------------------------------------- */
/* Many to many */
/* ----------------------------------------------------- */

-- black holes and stars
CREATE TABLE BlackHoleStar (
	BlackHoleId INT,
	StarId INT,
	OrbitTime DOUBLE,
	AvgOrbitDistance DOUBLE,
	PRIMARY KEY (BlackHoleId, StarId),
	FOREIGN KEY (BlackHoleId) REFERENCES BlackHole(BlackHoleId),
	FOREIGN KEY (StarId) REFERENCES Star(StarId)
);

-- stars and stars (binary systems)
CREATE TABLE StarStar (
	StarAId INT,
	StarBId INT,
	PRIMARY KEY (StarAId, StarBId),
	FOREIGN KEY (StarAId) REFERENCES Star(StarId),
	FOREIGN KEY (StarBId) REFERENCES Star(StarId)
);

-- stars and planets
CREATE TABLE StarPlanet (
	StarId INT,
	PlanetId INT,
	OrbitTime DOUBLE,
	AvgOrbitDistance DOUBLE,
	PRIMARY KEY (StarId, PlanetId),
	FOREIGN KEY (StarId) REFERENCES Star(StarId),
	FOREIGN KEY (PlanetId) REFERENCES Planet(PlanetId)
);

-- stars and elements
CREATE TABLE StarElement (
	StarId INT,
	NumProtons INT,
	PercentVolume DOUBLE,
	PercentMass DOUBLE,
	PRIMARY KEY (StarId, NumProtons),
	FOREIGN KEY (StarId) REFERENCES Star(StarId),
	FOREIGN KEY (NumProtons) REFERENCES Element(NumProtons)
);

-- planets and elements
CREATE TABLE PlanetElement (
	PlanetId INT,
	NumProtons INT,
	PercentVolume DOUBLE,
	PercentMass DOUBLE,
	PRIMARY KEY (PlanetId, NumProtons),
	FOREIGN KEY (PlanetId) REFERENCES Planet(PlanetId),
	FOREIGN KEY (NumProtons) REFERENCES Element(NumProtons)
);

-- moons and elements
CREATE TABLE MoonElement (
	MoonId INT,
	NumProtons INT,
	PercentVolume DOUBLE,
	PercentMass DOUBLE,
	PRIMARY KEY (MoonId, NumProtons),
	FOREIGN KEY (MoonId) REFERENCES Moon(MoonId),
	FOREIGN KEY (NumProtons) REFERENCES Element(NumProtons)
);


/* ----------------------------------------------------- */
/* Check the tables */
/* ----------------------------------------------------- */

SHOW TABLES;