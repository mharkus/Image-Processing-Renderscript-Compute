/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#pragma version(1)
#pragma rs java_package_name(com.example.android.rs.hellocompute)

rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

int mImageWidth;
const uchar4 *gInPixels;
uchar4 *gOutPixels;

float factor;
float bias;

float filterMatrix[] = {
	0, 0, 0,
	0, 1, 0,
	0, 0, 0
};

void root(const int32_t *v_in, int32_t  *v_out, const void *usrData, uint32_t x, uint32_t y) {
    int32_t row_index = *v_in;
    for ( int i = 0; i < mImageWidth; i++) {
        float4 pixel_colour = rsUnpackColor8888(gInPixels[i + row_index]);
        
        
        //float3 cur_colour = {0.0f, 0.0f, 0.0f};
        
        gOutPixels[i + row_index] = rsPackColorTo8888(pixel_colour); 
    }
}

void filter() {
    rsForEach(gScript, gIn, gOut, 0);
    
}

