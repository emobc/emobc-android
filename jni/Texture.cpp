/*==============================================================================
            Copyright (c) 2010-2012 QUALCOMM Austria Research Center GmbH.
            All Rights Reserved.
            Qualcomm Confidential and Proprietary
            
@file 
    Texture.cpp

@brief
    Implementation of class Texture.

==============================================================================*/

// Include files
#include "Texture.h"
#include "SampleUtils.h"

#include <string.h>

Texture::Texture() :
mWidth(0),
mHeight(0),
mChannelCount(0),
mData(0),
mTextureID(0)
{}


Texture::~Texture()
{
    if (mData != 0)
        delete [] mData;
}


Texture*
Texture::create(JNIEnv* env, jobject textureObject)
{

    Texture* newTexture = new Texture();

    // Handle to the Texture class:
    jclass textureClass = env->GetObjectClass(textureObject);

    // Get width:
    jfieldID widthID = env->GetFieldID(textureClass, "mWidth", "I");
    if (!widthID)
    {
        LOG("Field mWidth not found.");
        delete newTexture;
        return 0;
    }
    newTexture->mWidth = env->GetIntField(textureObject, widthID);

    // Get height:
    jfieldID heightID = env->GetFieldID(textureClass, "mHeight", "I");
    if (!heightID)
    {
        LOG("Field mHeight not found.");
        delete newTexture;
        return 0;
    }
    newTexture->mHeight = env->GetIntField(textureObject, heightID);

    // Always use RGBA channels:
    newTexture->mChannelCount = 4;

    // Get data:
    jmethodID texBufferMethodId = env->GetMethodID(textureClass , "getData", "()[B");
    if (!texBufferMethodId)
    {
        LOG("Function GetTextureBuffer() not found.");
        delete newTexture;
        return 0;
    }
    
    jbyteArray pixelBuffer = (jbyteArray)env->CallObjectMethod(textureObject, texBufferMethodId);    
    if (pixelBuffer == NULL)
    {
        LOG("Get image buffer returned zero pointer");
        delete newTexture;
        return 0;
    }

    jboolean isCopy;
    jbyte* pixels = env->GetByteArrayElements(pixelBuffer, &isCopy);
    if (pixels == NULL)
    {
        LOG("Failed to get texture buffer.");
        env->ReleaseByteArrayElements(pixelBuffer, pixels, 0);
        delete newTexture;
        return 0;
    }

    newTexture->mData = new unsigned char[newTexture->mWidth * newTexture->mHeight * newTexture->mChannelCount];

    unsigned char* textureData = (unsigned char*) pixels;

    int rowSize = newTexture->mWidth * newTexture->mChannelCount;
    for (int r = 0; r < newTexture->mHeight; ++r)
    {
        memcpy(newTexture->mData + rowSize * r, pixels + rowSize * (newTexture->mHeight - 1 - r), newTexture->mWidth * 4);
    }

    // Release:
    env->ReleaseByteArrayElements(pixelBuffer, pixels, 0);
    
    return newTexture;
}

