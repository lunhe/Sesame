package syncmap

import "sync"

type SyncMap struct {
	dataMap map[interface{}]interface{}
	sync.RWMutex
}

/**
copy一个当前map的副本
*/
func (m *SyncMap) Copy() map[interface{}]interface{} {
	m.Lock()
	defer m.Unlock()
	var duplicate = make(map[interface{}]interface{})
	for key, value := range m.dataMap {
		duplicate[key] = value
	}
	return duplicate
}

/**
copy一个当前map的所有key副本
*/
func (m *SyncMap) CopyKeys() []interface{} {
	m.Lock()
	defer m.Unlock()
	var keyDuplicate []interface{}
	for key, _ := range m.dataMap {
		if key != nil {
			keyDuplicate = append(keyDuplicate, key)
		}
	}
	return keyDuplicate
}

func (m *SyncMap) Set(key interface{}, val interface{}) {
	m.Lock()
	defer m.Unlock()
	m.dataMap[key] = val
}

func (m *SyncMap) Get(key interface{}) (val interface{}, ok bool) {
	m.Lock()
	defer m.Unlock()
	if v, exist := m.dataMap[key]; exist {
		return v, exist
	}
	return nil, false
}

func (m *SyncMap) Clear() {
	m.Lock()
	defer m.Unlock()
	m.dataMap = nil
	m.dataMap = NewSyncMap().dataMap
}

func (m *SyncMap) Delete(key interface{}) {
	m.Lock()
	defer m.Unlock()
	delete(m.dataMap, key)
}

func (m *SyncMap) Each(F func(key interface{}, val interface{}) bool) {
	m.Lock()
	defer m.Unlock()
	for key, val := range m.dataMap {
		if !F(key, val){
			break
		}
	}
}

func (m *SyncMap) Size() int {
	m.Lock()
	defer m.Unlock()
	return len(m.dataMap)
}

func NewSyncMap() *SyncMap {
	return &SyncMap{dataMap: make(map[interface{}]interface{})}
}

func (m *SyncMap) Exits(key interface{}) (ok bool) {
	m.Lock()
	defer m.Unlock()
	if _, exist := m.dataMap[key]; exist {
		return true
	}
	return false
}
