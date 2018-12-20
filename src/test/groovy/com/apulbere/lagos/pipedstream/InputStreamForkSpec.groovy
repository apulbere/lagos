package com.apulbere.lagos.pipedstream


import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.Executors

class InputStreamForkSpec extends Specification {
    def nrOutputFiles = 3
    def inputStreamFork = new ForkInputStream(Executors.newFixedThreadPool(5))

    @Shared
    def outFolder = "outTest"

    void setupSpec() {
        new File(outFolder).mkdir()
    }

    void cleanupSpec() {
        new File(outFolder).deleteDir()
    }

    def 'it reads file once and writes to multiple files'() {
        given:
            def sourceFile = this.getClass().getResource('/lipsum.txt')
            def originalTxt = sourceFile.getText().trim()

            def files = (1..nrOutputFiles).collect({ i -> "$outFolder/output${i}.txt"})
            def consumers = files.collect({ fileName -> new FileConsumer(fileName)})
        when:
            def futures = inputStreamFork.fork(sourceFile.openStream(), consumers)
            futures.collect({ f -> f.get() })
        then: "first file content matches the original one"
            originalTxt == new File(files[0]).text.trim()
        and: "as well as the rest of output files"
            files.drop(1).collect({fileName -> new File(fileName).getText()}).every({text -> text.trim() == originalTxt})
    }

    class FileConsumer extends CallableWithInputStream<Void> {
        String name

        FileConsumer(String name) {
            this.name = name
        }

        @Override
        Void call() throws Exception {
            new FileOutputStream(name).withCloseable { outputStream ->
                byte[] buffer = new byte[1024]
                while (true) {
                    int bytesRead = inputStream.read(buffer)
                    if (bytesRead == -1) break
                    outputStream.write(buffer, 0, bytesRead)
                }
            }
        }
    }
}
