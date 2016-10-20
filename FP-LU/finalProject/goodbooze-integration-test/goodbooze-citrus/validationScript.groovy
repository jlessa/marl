assert json.type == 'read'
assert json.mbean == 'java.lang:type=Memory'
assert json.attribute == 'HeapMemoryUsage'
assert json.value == '${heapUsage}'