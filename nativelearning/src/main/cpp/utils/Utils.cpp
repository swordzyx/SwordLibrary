#include "Utils.h"
bool isDebug = false; 

bool licenseValid() {
#ifdef TEST_MODE
    return true;
#else
    return false;
#endif
}



