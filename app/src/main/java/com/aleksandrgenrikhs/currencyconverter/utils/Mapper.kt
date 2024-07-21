package com.aleksandrgenrikhs.nivkhalphabet.utils

interface Mapper<I, O> {

     fun map(input: I): O
}