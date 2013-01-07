/*==============================================================================
            Copyright (c) 2010-2012 QUALCOMM Austria Research Center GmbH.
            All Rights Reserved.
            Qualcomm Confidential and Proprietary
            
@file 
    Texture.h

@brief
    A utility class for textures used in the samples.

==============================================================================*/
#ifndef _QCAR_TEXTURE_H_
#define _QCAR_TEXTURE_H_

// Include files
#include <jni.h>

// Forward declarations

/// A utility class for textures.
class Texture
{
public:

    /// Constructor
    Texture();

    /// Destructor.
    ~Texture();

    /// Returns the width of the texture.
    unsigned int getWidth() const;

    /// Returns the height of the texture.
    unsigned int getHeight() const;

    /// Create a texture from a jni object:
    static Texture* create(JNIEnv* env, jobject textureObject);
 
    /// The width of the texture.
    unsigned int mWidth;

    /// The height of the texture.
    unsigned int mHeight;

    /// The number of channels of the texture.
    unsigned int mChannelCount;

    /// The pointer to the raw texture data.
    unsigned char* mData;

    /// The ID of the texture
    unsigned int mTextureID;
    
private: 

    /// Hidden copy constructor
    Texture(const Texture &);      
           
    /// Hidden assignment operator
    Texture& operator= (const Texture &); 
    
};


#endif //_QCAR_TEXTURE_H_