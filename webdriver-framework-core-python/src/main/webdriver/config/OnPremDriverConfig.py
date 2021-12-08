class OnPremDriverConfigClass:
    #List<Platform>
    __platforms : list;

    def getPlatforms(self):
        return self.__platforms

    def setPlatforms(self, platforms):
        self.__platforms = platforms