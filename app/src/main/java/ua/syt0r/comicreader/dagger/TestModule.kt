package ua.syt0r.comicreader.dagger

import dagger.Module
import dagger.Provides
import ua.syt0r.comicreader.database.entity.DbFile

@Module
class TestModule {

    @Provides
    fun file(): DbFile = DbFile(0,"123",0,0,0,0,"zzz")

}