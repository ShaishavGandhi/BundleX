package com.shaishavgandhi.bundlex

import android.os.Bundle
import android.util.Size
import android.util.SparseArray
import com.shaishavgandhi.sample.*
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BundleXTest {

    @Test fun testString() {
        val bundle = Bundle()
        val sample = "hello world"
        bundle.putJavaString(sample)
        assertEquals(sample, bundle.getJavaString(""))
    }

    @Test fun testStringNull() {
        val bundle = Bundle()
        val default = ""
        assertEquals(default, bundle.getJavaString(default))
    }

    @Test fun testLong() {
        val bundle = Bundle()
        val sample = 789L
        bundle.putJavaLong(sample)

        assertEquals(sample, bundle.getJavaLong())
    }

    @Test fun testLongNull() {
        val bundle = Bundle()
        val default = 67L

        assertEquals(default, bundle.getJavaLong(default))
        assertEquals(0L, bundle.getJavaLong())
    }

    @Test fun testLongArray() {
        val bundle = Bundle()
        val sample = longArrayOf(13L, 14L, 15L)
        bundle.putJavaLongArray(sample)

        assertEquals(sample, bundle.getJavaLongArray())
    }

    @Test fun testLongArrayNull() {
        val bundle = Bundle()
        val default = longArrayOf(13L, 14L, 15L)

        assertEquals(default, bundle.getJavaLongArray(default))
    }

    @Test fun testInt() {
        val bundle = Bundle()
        val value = 12
        bundle.putJavaInt(value)

        assertEquals(value, bundle.getJavaInt())
    }

    @Test fun testIntNull() {
        val bundle = Bundle()
        val default = 12

        assertEquals(default, bundle.getJavaInt(default))
    }

    @Test fun testIntArray() {
        val bundle = Bundle()
        val sample = intArrayOf(13, 14, 15)
        bundle.putJavaIntArray(sample)

        assertEquals(sample, bundle.getJavaIntArray())
    }

    @Test fun testIntArrayNull() {
        val bundle = Bundle()
        val default = intArrayOf(13, 14, 15)

        assertEquals(default, bundle.getJavaIntArray(default))
    }

    @Test fun testDouble() {
        val bundle = Bundle()
        val value = 2.0
        bundle.putJavaDouble(value)

        assertEquals(value, bundle.getJavaDouble())
    }

    @Test fun testDoubleNull() {
        val bundle = Bundle()
        val default = 2.0

        assertEquals(default, bundle.getJavaDouble(default), 0.0)
    }

    @Test fun testDoubleArray() {
        val bundle = Bundle()
        val sample = doubleArrayOf(13.0, 14.0, 15.0)
        bundle.putJavaDoubleArray(sample)

        assertEquals(sample, bundle.getJavaDoubleArray())
    }

    @Test fun testDoubleArrayNull() {
        val bundle = Bundle()
        val default = doubleArrayOf(13.0, 14.0, 15.0)

        assertEquals(default, bundle.getJavaDoubleArray(default))
    }

    @Test fun testFloat() {
        val bundle = Bundle()
        val value = 2.0F
        bundle.putJavaFloat(value)

        assertEquals(value, bundle.getJavaFloat())
    }

    @Test fun testFloatNull() {
        val bundle = Bundle()
        val default = 2.0F

        assertEquals(default, bundle.getJavaFloat(default), 0.0F)
    }

    @Test fun testFloatArray() {
        val bundle = Bundle()
        val sample = floatArrayOf(13.0F, 14.0F, 15.0F)
        bundle.putJavaFloatArray(sample)

        assertEquals(sample, bundle.getJavaFloatArray())
    }

    @Test fun testFloatArrayNull() {
        val bundle = Bundle()
        val default = floatArrayOf(13.0F, 14.0F, 15.0F)

        assertEquals(default, bundle.getJavaFloatArray(default))
    }

    @Test fun testByte() {
        val bundle = Bundle()
        val value = Byte.MIN_VALUE
        bundle.putJavaByte(value)

        assertEquals(value, bundle.getJavaByte())
    }

    @Test fun testByteNull() {
        val bundle = Bundle()
        val default = Byte.MIN_VALUE

        assertEquals(default, bundle.getJavaByte(default))
    }

    @Test fun testByteArray() {
        val bundle = Bundle()
        val value = byteArrayOf(Byte.MAX_VALUE, Byte.MIN_VALUE)
        bundle.putJavaByteArray(value)

        assertEquals(value, bundle.getJavaByteArray())
    }

    @Test fun testByteArrayNull() {
        val bundle = Bundle()
        val default = byteArrayOf(Byte.MAX_VALUE, Byte.MIN_VALUE)

        assertEquals(default, bundle.getJavaByteArray(default))
    }


    // Short

    @Test fun testShort() {
        val bundle = Bundle()
        val value = Short.MIN_VALUE
        bundle.putJavaShort(value)

        assertEquals(value, bundle.getJavaShort())
    }

    @Test fun testShortNull() {
        val bundle = Bundle()
        val default = Short.MIN_VALUE

        assertEquals(default, bundle.getJavaShort(default))
    }

    @Test fun testShortArray() {
        val bundle = Bundle()
        val value = shortArrayOf(Short.MAX_VALUE, Short.MIN_VALUE)
        bundle.putJavaShortArray(value)

        assertEquals(value, bundle.getJavaShortArray())
    }

    @Test fun testShortArrayNull() {
        val bundle = Bundle()
        val default = shortArrayOf(Short.MAX_VALUE, Short.MIN_VALUE)

        assertEquals(default, bundle.getJavaShortArray(default))
    }

    // Chars

    @Test fun testChar() {
        val bundle = Bundle()
        val value = 'C'
        bundle.putJavaChar(value)

        assertEquals(value, bundle.getJavaChar())
    }

    @Test fun testCharNull() {
        val bundle = Bundle()
        val default = 'S'

        assertEquals(default, bundle.getJavaChar(default))
    }

    @Test fun testCharArray() {
        val bundle = Bundle()
        val value = charArrayOf('B', 'U', 'N', 'D', 'L', 'E')
        bundle.putJavaCharArray(value)

        assertEquals(value, bundle.getJavaCharArray())
    }

    @Test fun testCharArrayNull() {
        val bundle = Bundle()
        val default = charArrayOf('B', 'U', 'N', 'D', 'L', 'E')

        assertEquals(default, bundle.getJavaCharArray(default))
    }

    // Boolean

    @Test fun testBoolean() {
        val bundle = Bundle()
        val value = true
        bundle.putJavaBool(value)

        assertEquals(value, bundle.getJavaBool())
    }

    @Test fun testBooleanNull() {
        val bundle = Bundle()
        val default = false

        assertEquals(default, bundle.getJavaBool(default))
    }

    @Test fun testBooleanArray() {
        val bundle = Bundle()
        val value = booleanArrayOf(false, true, false)
        bundle.putJavaBoolArray(value)

        assertEquals(value, bundle.getJavaBoolArray())
    }

    @Test fun testBooleanArrayNull() {
        val bundle = Bundle()
        val default = booleanArrayOf(false, true, true, false)

        assertEquals(default, bundle.getJavaBoolArray(default))
    }

    // Charsequences

    @Test fun testCharSequence() {
        val bundle = Bundle()

        val charSequence = "hello there"
        bundle.putJavaCharSequence(charSequence)

        assertEquals(charSequence, bundle.getJavaCharSequence())
    }

    @Test fun testCharSequenceNull() {
        val bundle = Bundle()

        val default = "default"

        assertEquals(default, bundle.getJavaCharSequence(default))
    }

    @Test fun testCharSequenceArray() {
        val bundle = Bundle()
        val value = arrayOf<CharSequence>("bundle", "x")
        bundle.putJavaCharSequenceArray(value)

        assertArrayEquals(value, bundle.getJavaCharSequenceArray())
    }

    @Test fun testCharSequenceArrayNull() {
        val bundle = Bundle()
        val default = arrayOf<CharSequence>("bundle", "x")

        assertArrayEquals(default, bundle.getJavaCharSequenceArray(default))
    }

    @Test fun testCharSequenceArrayList() {
        val bundle = Bundle()
        val value = arrayListOf<CharSequence>("bundle", "x")
        bundle.putJavaCharSequenceList(value)

        assertEquals(value.size, bundle.getJavaCharSequenceList()?.size)
    }

    @Test fun testCharSequenceArrayListNull() {
        val bundle = Bundle()
        val default = arrayListOf<CharSequence>("bundle", "x")

        assertEquals(default.size, bundle.getJavaCharSequenceList(default).size)
    }

    @Test fun testSerializable() {
        val bundle = Bundle()
        val value = SampleSerializable("bundlex")
        bundle.putSampleSerializable(value)

        assertEquals(value, bundle.getSampleSerializable())
    }

    @Test fun testSerializableNull() {
        val bundle = Bundle()
        val default = SampleSerializable("bundlex")

        assertEquals(default, bundle.getSampleSerializable(default))
    }

    @Test fun testParcelable() {
        val bundle = Bundle()
        val value = KotlinParcelable(someProperty = "some",
            intProperty = "something else")
        bundle.putJavaParcelable(value)

        assertEquals(value, bundle.getJavaParcelable())
    }

    @Test fun testParcelableNull() {
        val bundle = Bundle()
        val default = KotlinParcelable(someProperty = "some",
            intProperty = "something else")

        assertEquals(default, bundle.getJavaParcelable(default))
    }

    @Test fun testParcelableList() {
        val bundle = Bundle()
        val value = arrayListOf(KotlinParcelable("one", "two"),
            KotlinParcelable("three", "four")
        )
        bundle.putJavaParcelableArrayList(value)

        assertEquals(value, bundle.getJavaParcelableArrayList())
    }

    @Test fun testParcelableListNull() {
        val bundle = Bundle()
        val default = arrayListOf(KotlinParcelable("one", "two"),
            KotlinParcelable("three", "four"))

        assertEquals(default, bundle.getJavaParcelableArrayList(default))
    }

    @Test fun testParcelableArray() {
        val bundle = Bundle()
        val value = arrayOf(KotlinParcelable("one", "two"),
            KotlinParcelable("three", "four")
        )
        bundle.putJavaParcelableArray(value)

        assertEquals(value, bundle.getJavaParcelableArray())
    }

    @Test fun testParcelableArrayNull() {
        val bundle = Bundle()
        val default = arrayOf(KotlinParcelable("one", "two"),
            KotlinParcelable("three", "four"))

        assertArrayEquals(default, bundle.getJavaParcelableArray(default))
    }

    @Test fun testSparseParcelableArray() {
        val bundle = Bundle()
        val value = SparseArray<KotlinParcelable>()
        value.put(1, KotlinParcelable("one", "two"))
        value.put(2, KotlinParcelable("three", "four"))
        bundle.putJavaParcelableSparseArray(value)

        assertEquals(value, bundle.getJavaParcelableSparseArray())
    }

}