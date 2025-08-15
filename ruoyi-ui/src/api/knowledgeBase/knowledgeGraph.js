import request from "@/utils/request";

export function findList(params) {
    return request({
        url: '/gpt/graphInfo/list',
        method: 'get',
        params
    })
}

export function syncGraph() {
    return request({
        url: '/gpt/graphInfo/sync',
        method: 'get'
    })
}

export function currUserAuthGraph() {
    return request({
        url: '/gpt/graphInfo/userGraphRole',
        method: 'get'
    })
}

export function findAuthGraphByRoleId(params) {
    return request({
        url: `/gpt/graphInfo/graphRole`,
        method: 'get',
        params
    })
}

export function authGraph(data) {
    return request({
        url: `/gpt/graphInfo/auth`,
        method: 'post',
        data
    })
}