import com.github.nscala_time.time.Imports._
object worksheet {
  val start = new DateTime(2013, 8, 23, 2, 23, 12)//> start  : org.joda.time.DateTime = 2013-08-23T02:23:12.000+09:00
  3323676                                         //> res0: Int(3323676) = 3323676
  start + 3317273.second                          //> res1: org.joda.time.DateTime = 2013-09-30T11:51:05.000+09:00
  start + 3323613.second                          //> res2: org.joda.time.DateTime = 2013-09-30T13:36:45.000+09:00
  start + 3323676.second                          //> res3: org.joda.time.DateTime = 2013-09-30T13:37:48.000+09:00
  start + 3340388.second                          //> res4: org.joda.time.DateTime = 2013-09-30T18:16:20.000+09:00
  start + 3340470.second                          //> res5: org.joda.time.DateTime = 2013-09-30T18:17:42.000+09:00
  start + 3340630.second                          //> res6: org.joda.time.DateTime = 2013-09-30T18:20:22.000+09:00
  start + 3340822.second                          //> res7: org.joda.time.DateTime = 2013-09-30T18:23:34.000+09:00
  start + 3340998.second                          //> res8: org.joda.time.DateTime = 2013-09-30T18:26:30.000+09:00
  start + 3341071.second                          //> res9: org.joda.time.DateTime = 2013-09-30T18:27:43.000+09:00
  DateTime.now.month(12)                          //> res10: org.joda.time.DateTime = 2013-12-02T10:40:19.910+09:00
}