package com.example

import java.io.IOException

import zio.{ App, ExitCode, URIO, ZEnv, ZIO }
import zio.console.{ getStrLn, putStrLn, Console }
import com.example.service._
import dependencyviaservice._

object Main extends App:
  val env = Console.live ++ (Console.live >>> DependencyViaService.live)
  val app: ZIO[Console with DependencyViaService, Throwable, Unit] =
    val result = 
      for
        _    <- putStrLn("What is your name?")
        name <- getStrLn
        ex1 <- dependencyviaservice.hello(name)
        svcname <- ex1
        out  <- putStrLn(s"Hello $name! $svcname")
      yield out
    result

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    app.provideCustomLayer(env).exitCode
    //app.provideLayer(env).exitCode
