import request from '@/utils/request'

// 查询所有资源信息
export function getResources() {
  return request({
    url: '/resource/allResources',
    method: 'get'
  })
}
