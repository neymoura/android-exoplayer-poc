package com.ioraptor.exoplayerpoc.aws


import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.rekognition.AmazonRekognition
import com.amazonaws.services.rekognition.AmazonRekognitionClient

object ClientFactory {

    fun createClient(): AmazonRekognition {
        val credentialsProvider = BasicAWSCredentials("$", "$")
        return AmazonRekognitionClient(credentialsProvider)
    }

}